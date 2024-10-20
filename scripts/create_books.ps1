$url = "http://localhost:8080/api/v1/book"
$count = 9

for ($i = 1; $i -le $count; $i++) {
  $bookData = @{
    title = "Das $i.Buch"
    author = "Autor $i"
    isbn = "234234233333$i"
    publisher = "Verlag $i"
    description = "Description $i"
    publishedDate = "2024-10-0$i"
  } | ConvertTo-Json

  Write-Host "Erstelle Buch Eintrag #$i"

  curl -Method POST -Uri $url -ContentType "application/json" -Body $bookData

  Write-Host
}