import pytest
from pyspark.sql import SparkSession


@pytest.fixture(scope='session')
def spark() -> SparkSession:
    session = SparkSession.builder \
        .master('local[2]') \
        .appName('kwik-e-mart-tests') \
        .getOrCreate()
    yield session
    session.stop()
