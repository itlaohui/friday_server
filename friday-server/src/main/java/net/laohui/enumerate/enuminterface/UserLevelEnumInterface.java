package net.laohui.enumerate.enuminterface;

public interface UserLevelEnumInterface {
    /**
     * 管理功能
     * @return
     */
    abstract boolean allowAdmin();

    /**
     * 系统功能
     * @return
     */
    abstract boolean allowSystem();

    /**
     * 用户基础功能
     * @return
     */
    abstract boolean allowUser();
}
