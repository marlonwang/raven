package net.logvv.raven.push.xiaomi.xmpush.server;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public enum PushJobType {
    Invalid(0),
    Topic(1),
    Common(2),
    Alias(3),
    BatchAlias(4),
    BatchRegId(5),
    ALL(6),
    UserAccount(7),
    BatchUserAccount(8);

    private final byte value;
    private static PushJobType[] VALID_JOB_TYPES;

    private PushJobType(int value) {
        this((byte)value);
    }

    private PushJobType(byte value) {
        this.value = value;
    }

    public byte value() {
        return this.value;
    }

    public static PushJobType from(byte value) {
        PushJobType[] var1 = VALID_JOB_TYPES;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            PushJobType type = var1[var3];
            if(type.value == value) {
                return type;
            }
        }

        return Invalid;
    }

    static {
        VALID_JOB_TYPES = new PushJobType[]{Topic, Common, Alias, UserAccount};
    }
}

