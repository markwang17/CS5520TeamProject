package edu.northestern.cs5520_teamproject_iamhere.entity;

public class StickerMessage {
    public Long date;
    public Long sticker_id;
    public String from_username;

    public Long getDate() {
        return date;
    }

    public Long getSticker_id() {
        return sticker_id;
    }

    public String getFrom_username() {
        return from_username;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public void setSticker_id(Long sticker_id) {
        this.sticker_id = sticker_id;
    }

    public void setFrom_username(String from_username) {
        this.from_username = from_username;
    }

    public StickerMessage() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public StickerMessage(Long date, Long sticker_id, String from_username) {
        this.date = date;
        this.sticker_id = sticker_id;
        this.from_username = from_username;
    }
}
