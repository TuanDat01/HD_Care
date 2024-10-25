package com.doctorcare.PD_project.dto.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class DoctorPageRequest implements Pageable {
    Integer limit;
    Integer offset;
    private final Sort sort;

    public DoctorPageRequest(Integer limit, Integer offset,Sort sort) {
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }

    public DoctorPageRequest(Integer limit, Integer offset) {
        this(limit,offset,Sort.unsorted());
    }

    @Override
    public int getPageNumber() {
        return offset/limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new DoctorPageRequest(getPageSize(),(int)(getOffset()+getPageSize()));
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? new DoctorPageRequest(getPageSize(), (int) (getOffset()-getPageSize())) : this;
    }

    @Override
    public Pageable first() {
        return new DoctorPageRequest(getPageSize(),0);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new DoctorPageRequest(getPageSize(),getPageNumber() * getPageSize());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }


}
