import com.example.playlistmaker.base_room.data.bd.AllTracksEntity
import com.example.playlistmaker.search.domain.modeles.Track

fun Track.mapToData(): AllTracksEntity {
    return AllTracksEntity(
        id = id,
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl,
    )
}

fun AllTracksEntity.mapToDomain(): Track {
    return Track(
        id = id,
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl,
    )
}



