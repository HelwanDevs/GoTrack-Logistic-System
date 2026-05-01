package com.gotrack.core_logistic.enums;



public enum ShipmentStatus {
      PendingPickup,
      InTransitToWarehouse,
      ArrivedAtWarehouse ,
      OutForDelivery ,
      InTransitToCustomer,
      DELIVERED;




      public boolean canTransitionToS(ShipmentStatus newStatus) {
    switch (this) {
        case PendingPickup:
            return newStatus == InTransitToWarehouse ;
        case InTransitToWarehouse:
            return newStatus == ArrivedAtWarehouse ;
        case ArrivedAtWarehouse:
            return newStatus == OutForDelivery;
        case OutForDelivery:
            return newStatus == InTransitToCustomer ;
        case InTransitToCustomer:
            return newStatus == DELIVERED ;
        default:
            return false;
    }
}
}