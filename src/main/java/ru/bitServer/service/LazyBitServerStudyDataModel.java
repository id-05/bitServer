package ru.bitServer.service;

import org.apache.commons.collections4.ComparatorUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.filter.FilterConstraint;
import org.primefaces.util.LocaleUtils;
import ru.bitServer.dao.BitServerStudy;

import javax.faces.context.FacesContext;
import java.beans.IntrospectionException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LazyBitServerStudyDataModel extends LazyDataModel<BitServerStudy> {

    private static final long serialVersionUID = 1L;

    private List<BitServerStudy> datasource;

    public LazyBitServerStudyDataModel(List<BitServerStudy> datasource) {
        this.datasource = datasource;
    }

    @Override
    public BitServerStudy getRowData(String rowKey) {
        for (BitServerStudy study : datasource) {
            if (study.getId() == Integer.parseInt(rowKey)) {
                return study;
            }
        }

        return null;
    }

    @Override
    public String getRowKey(BitServerStudy study) {
        return String.valueOf(study.getId());
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        return (int) datasource.stream()
                //.filter(o -> filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .count();
    }

    @Override
    public List<BitServerStudy> load(int offset, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        // apply offset & filters
        List<BitServerStudy> customers = datasource.stream()
                .skip(offset)
                //.filter(o -> filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .limit(pageSize)
                .collect(Collectors.toList());

        // sort
        if (!sortBy.isEmpty()) {
            List<Comparator<BitServerStudy>> comparators = sortBy.values().stream()
                    .map(o -> new LazySorter(o.getField(), o.getOrder()))
                    .collect(Collectors.toList());
            Comparator<BitServerStudy> cp = ComparatorUtils.chainedComparator(comparators); // from apache
            customers.sort(cp);
        }

        return customers;
    }

//    private boolean filter(FacesContext context, Collection<FilterMeta> filterBy, Object o) {
//        boolean matching = true;
//
//        for (FilterMeta filter : filterBy) {
//            FilterConstraint constraint = filter.getConstraint();
//            Object filterValue = filter.getFilterValue();
//
//            try {
//                Object columnValue = String.valueOf(ShowcaseUtil.getPropertyValueViaReflection(o, filter.getField()));
//                matching = constraint.isMatching(context, columnValue, filterValue, LocaleUtils.getCurrentLocale());
//            }
//            catch (ReflectiveOperationException | IntrospectionException e) {
//                matching = false;
//            }
//
//            if (!matching) {
//                break;
//            }
//        }
//
//        return matching;
//    }

}
