# Positional File

Writes a single fixed-width (positional) file from a CSV with Spark Shell.

## How to run

Needs Spark 3.0.x (`spark-shell`) on your machine.  
The dataset is not versioned: download [Avocado Prices](https://www.kaggle.com/neuromusic/avocado-prices) from Kaggle and save it as `data/avocado.csv`.

```bash
cd snippets/positional-file/

# Spark writes a folder (data/avocado/) with one part file, so rename it to a single file
spark-shell -I main.scala \
  && rm -f data/*.txt data/avocado/*.crc \
  && mv data/avocado/*.txt data/AVOCADO.TXT \
  && rm -rf data/avocado
```

The data path is relative, so run it from this folder. The result is `data/AVOCADO.TXT`.  
The `rm` commands only touch `.txt` files in `data/` and the `data/avocado/` output folder.

## Example

Contract (55 characters per line). Strings are padded with spaces, numbers with zeros.

| Order | Field         | Type     | Size | Observation                         |
|:-----:|---------------|----------|------|-------------------------------------|
| 1     | Date          | `string` | `08` | Date pattern `yyyyMMdd`.            |
| 2     | Average Price | `float`  | `12` | The last 2 digits are the decimals. |
| 3     | Total Volume  | `int`    | `10` |                                     |
| 4     | Region        | `string` | `25` | Must be in upper case.              |

Input record and expected line:

```yml
date: "2020-09-01"
average_price: 11.50
total_volume: 1050
region: "Brazil"
```

```text
202009010000000011500000001050                   BRAZIL
```

Output check (the missing line is the CSV header):

```bash
wc -l data/avocado.csv   # 18250
wc -l data/AVOCADO.TXT   # 18249
head -3 data/AVOCADO.TXT
# 201501040000000001220004087328                   ALBANY
# 201501040000000001790000137395                   ALBANY
# 201501040000000000100043502149                  ATLANTA
```

## References

- [Kaggle: Avocado Prices](https://www.kaggle.com/neuromusic/avocado-prices), thanks to [Justin Kiggins](https://www.kaggle.com/neuromusic) for sharing it
