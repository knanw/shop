$url = "http://localhost:8080/api/v1/series"
$count = 1

for ($i = 1; $i -le $count; $i++) {
    $seriesData = @{
        name = "Serie $i"
        genre = "Genre $i"
        description = "Beschreibung $i"
        books = @(
            @{ id = 1 },
            @{ id = 2 },
            @{ id = 3 },
            @{ id = 4 },
            @{ id = 5 }
        )
    } | ConvertTo-Json -Compress

    Write-Output "Erstelle Serie Eintrag #$i"
    Invoke-RestMethod -Uri $url -Method Post -ContentType "application/json" -Body $seriesData
    Write-Output "`n"
}
