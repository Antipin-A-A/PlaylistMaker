package com.example.playlistmaker.base_room.data.convector

import androidx.core.net.toUri
import com.example.playlistmaker.base_room.data.bd.PlayListEntity
import com.example.playlistmaker.playlist.domain.model.PlayList

class PlayListDbConvector {

    fun mapToData(playlist: PlayList): PlayListEntity {
        return PlayListEntity(
            playlist.id,
            playlist.listName,
            playlist.description,
            playlist.urlImage.toString(),
            playlist.listTracksId,
            playlist.countTracks,
        )
    }

    fun mapToDomain(playListEntity: PlayListEntity): PlayList {
        return PlayList(
            playListEntity.id,
            playListEntity.listName,
            playListEntity.description,
            playListEntity.urlImage?.toUri(),
            playListEntity.listTracksId,
            playListEntity.countTracks,
        )
    }
}