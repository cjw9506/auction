package com.auction.room.repository;

import com.auction.room.domain.AuctionRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRoomRepository extends JpaRepository<AuctionRoom, String> {
}