package ru.bitServer.service;

import org.primefaces.model.SortOrder;
import ru.bitServer.dao.BitServerStudy;

import java.util.Comparator;

public class LazySorter implements Comparator<BitServerStudy> {

    private String sortField;
    private SortOrder sortOrder;

    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(BitServerStudy study1, BitServerStudy study2) {
        try {
            Object value1 = study1.getSid();
            Object value2 = study2.getSid();

            int value = ((Comparable) value1).compareTo(value2);

            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
