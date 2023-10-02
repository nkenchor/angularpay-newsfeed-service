package io.angularpay.newsfeeds.models;

public enum ServiceType {
    CTO,
    PMT,
    SSV,
    PNB,
    PEF,
    PCH,
    OAP(false),
    SUP,
    MNL,
    STK,
    INV,
    ATS;

    private final boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    ServiceType() {
        this.enabled = true;
    }

    ServiceType(boolean enabled) {
        this.enabled = enabled;
    }
}
