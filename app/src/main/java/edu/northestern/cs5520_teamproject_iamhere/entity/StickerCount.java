package edu.northestern.cs5520_teamproject_iamhere.entity;

public class StickerCount {
    public long count;
    public long sticker_id;

    public StickerCount() {
    }

    public StickerCount(long count, long sticker_id) {
        this.count = count;
        this.sticker_id = sticker_id;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getSticker_id() {
        return sticker_id;
    }

    public void setSticker_id(long sticker_id) {
        this.sticker_id = sticker_id;
    }
}
