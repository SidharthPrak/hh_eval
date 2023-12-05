from datetime import datetime
import uuid
import asyncio
import json
import aiohttp

class HoneyHive:
    '''
    Class that sets up decorators for pipeline and metrics
    '''
    def __init__(self):
        self.data_request = {}
        pass

    async def async_process_inputs(self, config, input, function):
        start_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S.%f")[:-3]
        try:
            output = function(config, input)
        except:
            output = {}
        end_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S.%f")[:-3]
        metadata = {
            'config': config,
            'input': input,
            'output': output,
            'start_time': start_time,
            'end_time': end_time
        }
        return metadata

    async def async_model_iteration(self, configs, inputs, function):
        tasks = [self.async_process_inputs(config, input, function) for config in configs for input in inputs]
        # Wait for both tasks to complete
        return await asyncio.gather(*tasks)

    def set_pipeline(self, configs, inputs):
        '''
        :param configs:
        :param inputs:
        :return:

        Decorator function to set the pipeline
        '''

        def decorator(func):
            def wrapper():
                job_execution_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S.%f")[:-3]
                job_result = asyncio.run(self.async_model_iteration(configs, inputs, func))

                self.data_request = {
                    'request_id': str(uuid.uuid4()),
                    'job_execution_time': job_execution_time,
                    'job_result': job_result

                }
                print(self.data_request)

            return wrapper
        return decorator


    async def async_metric_calculation(self, result, function):
        try:
            if result.get('output'):
                metric_val = function(result.get('output'))
                result[function.__name__] = metric_val
        except:
            pass

    async def async_metric_iteration(self, function):
        tasks = [self.async_metric_calculation(result, function) for result in self.data_request.get("job_result")]
        # Wait for both tasks to complete
        return await asyncio.gather(*tasks)

    def set_metrics(self):
        '''
        :return:

        Decorator function to set the metrics. Will add each metric to the final object.
        '''
        def decorator(func):
            #
            def wrapper():
                if not self.data_request:
                    raise "Pipeline not defined yet."
                job_result = asyncio.run(self.async_metric_iteration(func))

                print(self.data_request)

            return wrapper
        return decorator

    async def post_data(self, url):
        headers = {"Content-Type": "application/json"}
        async with aiohttp.ClientSession() as session:
            async with session.post(url, data = json.dumps(self.data_request), headers = headers) as response:
                return await response.text()

    async def push_to_kafka(self):
        url = "http://localhost:8080/kafka_push/metrics"
        response = await self.post_data(url)
        print("Response,", response)

    def push(self):
        # mocking a Kafka message
        asyncio.run(self.push_to_kafka())





