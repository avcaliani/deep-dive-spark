from pipelines.base import Pipeline
from pipelines.kwik_e_mart.sales import SalesPipeline

PIPELINES: dict[str, type[Pipeline]] = {
    'kwik-e-mart-sales': SalesPipeline,
}

__all__ = ['Pipeline', 'SalesPipeline', 'PIPELINES']
