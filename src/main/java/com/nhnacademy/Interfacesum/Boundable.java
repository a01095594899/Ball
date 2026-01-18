package com.nhnacademy.Interfacesum;

public interface Boundable {
    Bounds getBounds();

    default boolean isColliding(Boundable other) {
        if (other == null) {
            return false;
        }
        return getBounds().intersects(other.getBounds());
    }

}
