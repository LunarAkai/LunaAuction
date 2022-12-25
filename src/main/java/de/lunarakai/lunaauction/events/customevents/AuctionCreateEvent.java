package de.lunarakai.lunaauction.events.customevents;

import de.lunarakai.lunaauction.LunaAuction;
import de.lunarakai.lunaauction.sql.Database;
import de.lunarakai.lunaauction.sql.DatabaseQueries;
import de.lunarakai.lunaauction.utils.ChatBuilder;
import de.lunarakai.lunaauction.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class AuctionCreateEvent extends Event implements Cancellable {

    public static boolean sqlSuccess = false;
    private static final HandlerList HANDLERS = new HandlerList();
    private String playerName = null;
    private UUID uuid = null;
    private boolean isCancelled;
    private Integer playerId;
    private String itemStack;
    private PersistentDataContainer itemData;
    private int currentPrice = 0;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public AuctionCreateEvent(UUID pUuid, String playerName) throws SQLException, IOException {
        if(Database.isConnected()) {
            this.uuid = pUuid;
            this.playerName = playerName;
            LunaAuction.LOGGER.info("UUID: " + uuid + " Player Name: " + playerName);

            //only necessary if player doesn't already exists in DB, if so create new Entry (ID) for player
            ResultSet checkPlayer = DatabaseQueries.equalsQuery("UUID", "players", "UUID", String.valueOf(uuid));
            if(checkPlayer.next()) {
                createPlayerAuctionEntry(uuid, playerName);
                LunaAuction.LOGGER.info("Player already exists");
            }

            //get Player ID
            this.playerId = getPlayerIDInDatabase(uuid);

            Player player = Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(String.valueOf(uuid))));

            //Item Data from Item in Main Hand
            if(ItemUtil.getItemNBTInHand(uuid) != null){
                this.itemStack = ItemUtil.serialize(ItemUtil.getItemNameInHand(uuid));
                this.itemData = ItemUtil.getItemNBTInHand(uuid).getPersistentDataContainer();
                createAuctionEntry(playerId, itemStack, itemData, currentPrice);


                //TODO take items from player
                //TODO get Auction ID

                ChatBuilder chatBuilder = new ChatBuilder();
                chatBuilder.sendSuccessMessage(player, "Auction successfully created!");
                chatBuilder.sendSuccessfulAuctionCreationMessage(player, itemStack, currentPrice);


            } else {
                ChatBuilder chatBuilder = new ChatBuilder();
                chatBuilder.sendWarningMessage(player, "You must have the item(s) you want to sell in the main hand!");
            }


            //ChatBuilder chatBuilder = new ChatBuilder();
            //chatBuilder.sendWarningMessage(Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(String.valueOf(uuid)))), "Error in auction creation.");

            this.isCancelled = false;
        } else {
            LunaAuction.LOGGER.warning("DATABASE ISN'T CONNECTED!");
            LunaAuction.LOGGER.warning("Retrying to connect... ");
            Database.connect();
            this.isCancelled = false;
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    private Integer getPlayerIDInDatabase(UUID _uuid) throws SQLException {
        playerId = Database.getIdFromTable(_uuid);

        return playerId;
    }

    private void createPlayerAuctionEntry(UUID _Uuid, String _playerName) throws SQLException {
        Database.insertPlayerData("players", _Uuid, _playerName);
    }

    private void createAuctionEntry(Integer _playerID, String _itemStack, PersistentDataContainer _itemNBT, Integer _currentPrice) {
        Database.insertAuctionData("auctions", _playerID, _itemStack, _itemNBT, _currentPrice);
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
