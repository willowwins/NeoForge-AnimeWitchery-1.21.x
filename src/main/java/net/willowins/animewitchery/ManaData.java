package net.willowins.animewitchery;

public record ManaData(int current, int max) {
    // Helper methods to modify mana while keeping values within bounds
    public ManaData addMana(int amount) {
        int newMana = Math.clamp(this.current + amount, 0, this.max);
        return new ManaData(newMana, this.max);
    }
}
