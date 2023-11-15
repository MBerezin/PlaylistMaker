package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.entity.PlaylistTrackEntity

@Dao
interface PlaylistTrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTrack(track: PlaylistTrackEntity): Long

    @Query("SELECT * FROM playlist_track_table WHERE playlistId = :id")
    suspend fun getTracksByPlaylistId(id: Int): List<PlaylistTrackEntity>

    @Query("DELETE FROM playlist_track_table WHERE playlistId = :playlistId AND id = :trackId")
    suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int)

    @Query("DELETE FROM playlist_track_table WHERE playlistId = :playlistId")
    suspend fun deleteTracksByPlaylistId(playlistId: Int)

}
