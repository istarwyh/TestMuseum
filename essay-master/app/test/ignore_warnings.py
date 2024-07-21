import unittest
import warnings


class IgnoreWarningsTestRunner(unittest.TextTestRunner):
    def run(self, test):
        with warnings.catch_warnings():
            warnings.simplefilter("ignore", category=DeprecationWarning)
            return super().run(test)
