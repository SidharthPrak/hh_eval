from honeyhive import HoneyHive


if __name__ == '__main__':
    honeyhive = HoneyHive()

    configs = [
        {
            "model": "gpt-3.5-turbo",
            "temperature": 0.5
        },
        {
            "model": "gpt-4",
            "temperature": 0
        }

    ]

    inputs = [
        {
            "topic": "enterprise SaaS pitch",
            "input_metadata": {
                "importance": 0.9
            }
        },
        {
            "topic": "marketing campaign",
            "input_metadata": {
                "importance": 0.5
            }
        }
    ]


    @honeyhive.set_pipeline(configs, inputs)
    def functioncheck(config, input):
        print(config, input)
        return config.get("model") + input.get("topic")

    @honeyhive.set_metrics()
    def metric(output):
        return len(output)

    @honeyhive.set_metrics()
    def metric2(output):
        return len(output) -1


    functioncheck()
    metric()
    metric2()

    honeyhive.push()

