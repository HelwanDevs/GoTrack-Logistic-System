package com.gotrack.core_logistic.enums;

public enum ShipmentStatus {
    PendingPickup,
    InTransitToWarehouse,
    ArrivedAtWarehouse,
    OutForDelivery,
    InTransitToMERCHANT,
    DELIVERED;




      public boolean canTransitionToS(ShipmentStatus newStatus) {
          return switch (this) {
              case PendingPickup -> newStatus == InTransitToWarehouse;
              case InTransitToWarehouse -> newStatus == ArrivedAtWarehouse;
              case ArrivedAtWarehouse -> newStatus == OutForDelivery;
              case OutForDelivery -> newStatus == InTransitToMERCHANT;
              case InTransitToMERCHANT -> newStatus == DELIVERED;
              default -> false;
          };
}
}