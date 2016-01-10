package com.evs.doctor.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class PublicationType implements Identify<Integer>, Comparable<PublicationType> {

    private Integer id;
    private String title;
    private String description;
    /**может ли доктор создавать данный тип публикации*/
    private boolean doctorCanCreate;
    /**может ли доктор выбирать этот тип в фильтре*/
    private boolean doctorCanSelect;
    /**порядок в списке выбора, если этот тип можно выбирать*/
    private int selectOrderIndex;
    /**нужно ли указывать специальность при создании публикации данного типа*/
    private boolean specialtyRequired;

    /**
     * @param id the id to set
     */
    public final void setId(Integer id) {
        this.id = id;
    }

    /**{@inheritDoc}*/
    @JsonProperty(value = "id")
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * @return the title
     */
    @JsonProperty(value = "title")
    public final String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public final void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    @JsonProperty(value = "description")
    public final String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public final void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the doctorCanCreate
     */
    @JsonProperty(value = "doctorCanCreate")
    public final boolean isDoctorCanCreate() {
        return doctorCanCreate;
    }

    /**
     * @param doctorCanCreate the doctorCanCreate to set
     */
    public final void setDoctorCanCreate(boolean doctorCanCreate) {
        this.doctorCanCreate = doctorCanCreate;
    }

    /**
     * @return the doctorCanSelect
     */
    @JsonProperty(value = "doctorCanSelect")
    public final boolean isDoctorCanSelect() {
        return doctorCanSelect;
    }

    /**
     * @param doctorCanSelect the doctorCanSelect to set
     */
    public final void setDoctorCanSelect(boolean doctorCanSelect) {
        this.doctorCanSelect = doctorCanSelect;
    }

    /**
     * @return the selectOrderIndex
     */
    @JsonProperty(value = "selectOrderIndex")
    public final int getSelectOrderIndex() {
        return selectOrderIndex;
    }

    /**
     * @param selectOrderIndex the selectOrderIndex to set
     */
    public final void setSelectOrderIndex(int selectOrderIndex) {
        this.selectOrderIndex = selectOrderIndex;
    }

    /**
     * @return the specialtyRequired
     */
    @JsonProperty(value = "specialtyRequired")
    public final boolean isSpecialtyRequired() {
        return specialtyRequired;
    }

    /**
     * @param specialtyRequired the specialtyRequired to set
     */
    public final void setSpecialtyRequired(boolean specialtyRequired) {
        this.specialtyRequired = specialtyRequired;
    }

    @Override
    public int compareTo(PublicationType publicationType) {
        if (getSelectOrderIndex() < publicationType.getSelectOrderIndex()) {
            return -1;
        } else if (getSelectOrderIndex() == publicationType.getSelectOrderIndex()) {
            return 0;
        } else {
            return 1;
        }
    }
}
