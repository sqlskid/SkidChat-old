package xyz.sqlskid.skidchat.server.client;

public enum Rank {
    DEV(99),
    ADMIN(4),
    MOD(3),
    VIP(2),
    MEMBER(1),
    GUEST(0);

    private int lvl;

    Rank(int lvl){
        this.lvl = lvl;
    }

    public int getLvl() {
        return lvl;
    }
}
