package com.gotrack.core_logistic.enums;


public enum PickupStatus {
    Pending,
    Accepted,
    Completed,
    CurierAssigned,
    Cancelled;



    public boolean canTransitionTo(PickupStatus newStatus) {
        return switch (this) {
            case Pending -> newStatus == Accepted || newStatus == Cancelled;
            case Accepted -> newStatus == Completed || newStatus == CurierAssigned || newStatus == Cancelled;
            case CurierAssigned -> newStatus == Completed || newStatus == Cancelled;
            default -> false;
        };
}
}


