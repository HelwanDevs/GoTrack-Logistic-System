package com.gotrack.core_logistic.enums;



public enum ShipmentStatus {
      PendingPickup,
      InTransitToWarehouse,
      ArrivedAtWarehouse ,
      OutForDelivery ,
      InTransitToCustomer,
      DELIVERED;




      public boolean canTransitionToS(ShipmentStatus newStatus) {
          return switch (this) {
              case PendingPickup -> newStatus == InTransitToWarehouse;
              case InTransitToWarehouse -> newStatus == ArrivedAtWarehouse;
              case ArrivedAtWarehouse -> newStatus == OutForDelivery;
              case OutForDelivery -> newStatus == InTransitToCustomer;
              case InTransitToCustomer -> newStatus == DELIVERED;
              default -> false;
          };
}
}