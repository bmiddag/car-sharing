package models;

import objects.Damage;
import objects.Refueling;
import objects.Ride;
import objects.DamageDoc;

import java.util.List;
import java.util.Map;

/**
 * Wrapper for a Ride, containing all relevant information (e.g. refuelings, damage proofs, etc.)
 */
public class RideWrapper {

    public Ride ride;
    public Map<Damage, List<DamageDoc>> damages;
    public List<Refueling> refuelings;
}
