
package com.gotrack.core_logistic.finance_service.enums;


public enum PickupStatus {
    Pending,
    Accepted,
    Completed,
    CurierAssigned,
    Cancelled;



    public boolean canTransitionTo(PickupStatus newStatus) {
    switch (this) {
        case Pending:
            return newStatus == Accepted || newStatus == Cancelled;
        case Accepted:
            return newStatus == Completed || newStatus == CurierAssigned || newStatus == Cancelled;
        case CurierAssigned:
            return newStatus == Completed || newStatus == Cancelled;
        default:
            return false;
    }
}
}


