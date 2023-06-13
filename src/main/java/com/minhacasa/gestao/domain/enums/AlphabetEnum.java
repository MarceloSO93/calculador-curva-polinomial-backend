package com.minhacasa.gestao.domain.enums;


public enum AlphabetEnum {

    A(1),
    B(2),
    C(3),
    D(4),
    E(5),
    F(6),
    G(7),
    H(8),
    I(9),
    J(10),
    K(11),
    L(12),
    M(13),
    N(14),
    O(15),
    P(16),
    Q(17),
    R(18),
    S(19),
    T(20),
    U(21),
    V(22),
    X(23),
    W(24),
    Y(25),
    Z(26);

    private final int index;

    AlphabetEnum(int index) {
        this.index = index;
    }

    public static int getIndex(int index) {
        return index;
    }

    public static AlphabetEnum AlphabetEnumByIndex(int index) {
        for (AlphabetEnum key : AlphabetEnum.values()) {
            if (key.index == index) {
                return key;
            }
        }
        return AlphabetEnum.Z;
    }
}
