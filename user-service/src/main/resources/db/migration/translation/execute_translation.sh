#!/bin/bash

# PostgreSQL connection details
PG_HOST=""
PG_PORT=""
PG_DB=""
PG_USER="postgres"
PG_PASSWORD=""

# Define the translation mapping file
TRANSLATION_FILE=""

# Read translation mappings from text file into an associative array
declare -A translations
while IFS='=' read -r key value; do
    translations["$key"]="$value"
done < "$TRANSLATION_FILE"
# Define the SQL script
SQL_SCRIPT=""

# Create a temporary file for the processed SQL script 
TEMP_SQL_SCRIPT=$(mktemp)

# Prin log: Before translation
echo "Debug: Before translation"
echo "=========================="
cat "$SQL_SCRIPT"
echo "=========================="
# Loop through each line in the input SQL script
while IFS= read -r line; do
    # Check if any special characters exist in the line
    if [[ "$line" =~ \${[^}]*[\\/\(\)\[\]\.\*\^\$][^}]*} ]]; then
        # If special characters are found, directly replace them in the line
        for key in "${!translations[@]}"; do
            if [[ "${translations[$key]}" ]]; then
                # Replace the placeholder with the value (direct replacement)
                line=$(sed "s/\"\${$key}\"/'${translations[$key]}'/g" <<< "$line")
            fi
        done
    else
        # If no special characters are found, escape both key and value before replacement
        for key in "${!translations[@]}"; do
            if [[ "${translations[$key]}" ]]; then
                # Escape special characters in the key and value
                escaped_key=$(sed 's/[&/\]/\\&/g' <<< "$key")
                escaped_value=$(sed 's/[&/\]/\\&/g' <<< "${translations[$key]}")
                # Replace the placeholder with the escaped value
                line=$(sed "s/\"\${$escaped_key}\"/'${escaped_value}'/g" <<< "$line")
            fi
        done
    fi
    # Write the replaced line to the temporary SQL script
    echo "$line" >> "$TEMP_SQL_SCRIPT"
done < "$SQL_SCRIPT"
# Print  log: After translation
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

# Print debug log: After executing SQL commands
echo "Debug: After executing SQL commands"
echo "======================================"
echo "SQL commands executed successfully."
echo "======================================"

# Remove the temporary SQL script
rm "$TEMP_SQL_SCRIPT"