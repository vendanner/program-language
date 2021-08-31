from pyflink.table import DataTypes
from pyflink.table.udf import udtf


# @udf(result_type=DataTypes.BIGINT())
# def add_one(i):
#     return i + 1

from pyflink.table import DataTypes
from pyflink.table.udf import udtf


@udtf(result_types=[DataTypes.INT()])
def add_one(i: int):
    result = i + 1
    yield result