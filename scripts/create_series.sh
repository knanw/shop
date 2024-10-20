#!/bin/bash

URL="http://localhost:8080/api/v1/series"
COUNT=1

for i in $(seq 1 $COUNT)
do
  SERIES_DATA=$(cat <<EOF
{
  "name": "Serie $i",
  "genre": "Genre $i",
  "description": "Beschreibung $i",
  "books": [
    {
      "id": 1
    },
    {
      "id": 2
    },
    {
      "id": 3
    },
    {
      "id": 4
    },
    {
      "id": 5
    }
  ]
}
EOF
  )

  echo "Erstelle Serie Eintrag #$i"
  curl -X POST -H "Content-Type: application/json" -d "$SERIES_DATA" "$URL"
  echo -e "\n"
done

#chmod +x create_series.sh
#./create_series.sh