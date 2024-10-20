#!/bin/bash

URL="http://localhost:8080/api/v1/book"

COUNT=9

for i in $(seq 1 $COUNT)
do
  BOOK_DATA=$(cat <<EOF
{
  "title": "Das $i.Buch",
  "author": "Autor $i",
  "isbn": "3-86680-192-$i",
  "publisher": "Verlag $i",
  "description": "Description $i",
  "publishedDate": "2024-10-0$i"
}
EOF
  )

  echo "Erstelle Buch Eintrag #$i"
  curl -X POST -H "Content-Type: application/json" -d "$BOOK_DATA" "$URL"
  echo -e "\n"
done

#chmod +x create_books.sh
#./create_books.sh