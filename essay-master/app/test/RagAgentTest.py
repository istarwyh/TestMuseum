import unittest
from agentuniverse.agent.agent import Agent
from agentuniverse.agent.agent_manager import AgentManager
from agentuniverse.agent.output_object import OutputObject
from agentuniverse.base.agentuniverse import AgentUniverse

from app.test.ignore_warnings import IgnoreWarningsTestRunner


class RagAgentTest(unittest.TestCase):
    """
    Test cases for the rag agent
    """

    def setUp(self) -> None:
        AgentUniverse().start(config_path='../../config/config.toml')

    def test_rag_agent(self):
        """Test demo rag agent."""
        instance: Agent = AgentManager().get_instance_obj('demo_rag_agent')
        output_object: OutputObject = instance.run(input='纳斯达克为什么大崩？')
        print(output_object.get_data('output'))


if __name__ == '__main__':
    unittest.main(testRunner=IgnoreWarningsTestRunner)
