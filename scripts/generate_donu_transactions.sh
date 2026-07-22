#!/bin/bash
# Generates a mock "DonutCoin (DONU)" transaction ledger dataset, themed on
# The Simpsons, as CSV part-files under data/bronze/donu-transactions/.
# Adapted from kafka-in-docker/scripts/donu-transactions.sh, but writes CSV
# rows instead of publishing Kafka messages, and avoids all per-row
# subprocess forking so it can generate ~1M rows in well under a minute:
#   - transaction_id: read directly from /proc/sys/kernel/random/uuid
#     (a bash builtin file read, not the `uuidgen` binary)
#   - timestamp: bash's builtin printf '%()T' directive + $EPOCHSECONDS
#     (not the external `date` binary)
#   - amount_cents / fee_cents: bash native integer arithmetic $(( ))
#
# Env vars: NUM_TRANSACTIONS (default 1000000), NUM_FILES (default 10),
# DATA_PATH (default data). NUM_FILES auto-caps down if NUM_TRANSACTIONS is
# too small to fill it. Linux-specific, requires Bash >= 5.0 -- fine, only
# runs inside the project's Docker container. $RANDOM isn't seeded, so
# re-runs produce a different (but similarly-shaped) dataset each time.

set -euo pipefail

readonly NUM_TRANSACTIONS="${NUM_TRANSACTIONS:-1000000}"
readonly NUM_FILES_REQUESTED="${NUM_FILES:-10}"
readonly DATA_PATH="${DATA_PATH:-data}"
readonly MIN_ROWS_PER_FILE=1000
readonly OUTPUT_DIR="${DATA_PATH}/bronze/donu-transactions"
readonly INVALID_PCT=3

readonly USERS=(
    "Homer Simpson" "Marge Simpson" "Bart Simpson" "Lisa Simpson"
    "Maggie Simpson" "Ned Flanders" "Milhouse Van Houten" "Mr. Burns"
    "Waylon Smithers" "Krusty the Clown" "Apu Nahasapeemapetilon"
    "Principal Skinner" "Edna Krabappel" "Chief Wiggum" "Ralph Wiggum"
    "Groundskeeper Willie" "Comic Book Guy" "Moe Szyslak" "Lenny Leonard"
    "Carl Carlson"
)
readonly NUM_USERS="${#USERS[@]}"
readonly UNKNOWN_USER="Snowball (Unknown)"

if [ "${NUM_TRANSACTIONS}" -lt 1 ]; then
    echo "generate_donu_transactions: NUM_TRANSACTIONS must be >= 1 (got ${NUM_TRANSACTIONS})" >&2
    exit 1
fi
if [ "${NUM_FILES_REQUESTED}" -lt 1 ]; then
    echo "generate_donu_transactions: NUM_FILES must be >= 1 (got ${NUM_FILES_REQUESTED})" >&2
    exit 1
fi

max_files_by_rows=$(( NUM_TRANSACTIONS / MIN_ROWS_PER_FILE ))
if [ "${max_files_by_rows}" -lt 1 ]; then
    max_files_by_rows=1
fi
if [ "${NUM_FILES_REQUESTED}" -le "${max_files_by_rows}" ]; then
    NUM_FILES="${NUM_FILES_REQUESTED}"
else
    NUM_FILES="${max_files_by_rows}"
    echo "generate_donu_transactions: capping NUM_FILES ${NUM_FILES_REQUESTED} -> ${NUM_FILES}" >&2
fi

mkdir -p "${OUTPUT_DIR}"

new_row() {
    local from_idx=$(( RANDOM % NUM_USERS ))
    local to_idx=$(( RANDOM % NUM_USERS ))
    local from_user="${USERS[$from_idx]}"
    local to_user="${USERS[$to_idx]}"
    local amount_cents=$(( (RANDOM % 999901) + 100 ))
    local fee_cents=$(( amount_cents / 20 ))

    if (( (RANDOM % 100) < INVALID_PCT )); then
        case $(( RANDOM % 3 )) in
            0) to_user="${from_user}" ;;
            1) if (( RANDOM % 2 == 0 )); then
                   from_user="${UNKNOWN_USER}"
               else
                   to_user="${UNKNOWN_USER}"
               fi ;;
            2) amount_cents=$(( -amount_cents ))
               fee_cents=$(( amount_cents / 20 )) ;;
        esac
    fi

    local uuid
    read -r uuid < /proc/sys/kernel/random/uuid

    local epoch=$(( EPOCHSECONDS - (RANDOM % (90 * 24 * 3600)) ))
    local timestamp
    printf -v timestamp '%(%Y-%m-%dT%H:%M:%SZ)T' "${epoch}"

    printf '%s,%s,%s,%s,%d,%s,%d\n' \
        "${uuid}" "${timestamp}" "${from_user}" "${to_user}" "${amount_cents}" "DONU" "${fee_cents}"
}

base_rows=$(( NUM_TRANSACTIONS / NUM_FILES ))
extra_rows=$(( NUM_TRANSACTIONS % NUM_FILES ))

for (( file_idx = 0; file_idx < NUM_FILES; file_idx++ )); do
    rows_in_file=${base_rows}
    if (( file_idx < extra_rows )); then
        rows_in_file=$(( rows_in_file + 1 ))
    fi

    part_file=$(printf '%s/part-%05d.csv' "${OUTPUT_DIR}" "${file_idx}")
    {
        echo "transaction_id,timestamp,from_user,to_user,amount_cents,currency,fee_cents"
        for (( row = 0; row < rows_in_file; row++ )); do
            new_row
        done
    } > "${part_file}"
    echo "generate_donu_transactions: wrote ${rows_in_file} rows -> ${part_file}" >&2
done

echo "generate_donu_transactions: done. NUM_TRANSACTIONS=${NUM_TRANSACTIONS} NUM_FILES=${NUM_FILES} -> ${OUTPUT_DIR}" >&2
