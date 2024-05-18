#!/bin/bash

# Enable debugging
set -x

# Set PostgreSQL host and port environment variables
export PG_HOST="" # Replace with your PostgreSQL host address
export PG_PORT=""  # Replace with your PostgreSQL port number

# Other environment variables
PG_DB=""
PG_USER=""
TEMP_SQL_SCRIPT=""  # Specify the path to your temporary SQL script
SQL_SCRIPT=""      # Specify the path to your original SQL script

# Read translations from a separate file (assuming translations are stored as key-value pairs)
translations_file=""  # Specify the path to your translations file
declare -A translations
while IFS='=' read -r key value; do
    translations["$key"]="$value"
done < "$translations_file"

# Print debug log: Before translation
echo "Debug: Before translation"
echo "=========================="
cat "$SQL_SCRIPT"
echo "=========================="

# Process the SQL script to replace placeholders with translations
while IFS= read -r line; do
    # Loop through each translation key-value pair
    for key in "${!translations[@]}"; do
        # Escape special characters in the key and value
        ESC_KEY=$(sed 's/[&/\]/\\&/g' <<< "$key")
        ESC_VAL=$(sed 's/[&/\]/\\&/g' <<< "${translations[$key]}")
        # Replace the placeholder with the escaped value
        line=$(sed "s/\"\${$ESC_KEY}\"/'${ESC_VAL}'/g" <<< "$line")
    done < <(grep -oP '\${\K[^}]+(?=})' <<< "$line")
    # Write the replaced line to the temporary SQL script
    echo "$line" >> "$TEMP_SQL_SCRIPT"
done < "$SQL_SCRIPT"

# Print log: After translation
echo "Debug: After translation"
echo "=========================="
cat "$TEMP_SQL_SCRIPT"
echo "=========================="

# Construct psql command
PSQL_COMMAND="psql -h $PG_HOST -p $PG_PORT -d $PG_DB -U $PG_USER -W -f $TEMP_SQL_SCRIPT"

# Print debug log: Before executing SQL commands
echo "Debug: Before executing SQL commands"
echo "======================================"
echo "Executing translations..."
echo "Executing the following SQL commands:"
cat "$TEMP_SQL_SCRIPT"
echo "======================================"

# Execute psql command
$PSQL_COMMAND