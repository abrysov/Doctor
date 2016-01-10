package com.evs.doctor.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Specialties implements Identify<Integer>, Comparable<Specialties> {

    private Integer id;
    /**   название специальности*/
    private String name;
    /**для обращения в единственном числе*/
    private String addressToDoctor1;
    /**для обращения во множественном числе, если докторов >= 2*/
    private String addressToDoctor2;
    /**для обращения во множественном числе, если докторов >= 5*/
    private String addressToDoctor5;
    /**порядок в списке выбора, если этот тип можно выбирать*/
    private int selectOrderIndex;

    /**{@inheritDoc}*/
    @Override
    @JsonProperty(value = "id")
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public final void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    @JsonProperty(value = "name")
    public final String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public final void setName(String name) {
        this.name = name;
    }

    /**
     * @return the addressToDoctor1
     */
    @JsonProperty(value = "addressToDoctor1")
    public final String getAddressToDoctor1() {
        return addressToDoctor1;
    }

    /**
     * @param addressToDoctor1 the addressToDoctor1 to set
     */
    public final void setAddressToDoctor1(String addressToDoctor1) {
        this.addressToDoctor1 = addressToDoctor1;
    }

    /**
     * @return the addressToDoctor2
     */
    @JsonProperty(value = "addressToDoctor2")
    public final String getAddressToDoctor2() {
        return addressToDoctor2;
    }

    /**
     * @param addressToDoctor2 the addressToDoctor2 to set
     */
    public final void setAddressToDoctor2(String addressToDoctor2) {
        this.addressToDoctor2 = addressToDoctor2;
    }

    /**
     * @return the addressToDoctor5
     */
    @JsonProperty(value = "addressToDoctor5")
    public final String getAddressToDoctor5() {
        return addressToDoctor5;
    }

    /**
     * @param addressToDoctor5 the addressToDoctor5 to set
     */
    public final void setAddressToDoctor5(String addressToDoctor5) {
        this.addressToDoctor5 = addressToDoctor5;
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

    @Override
    public int compareTo(Specialties specialties) {
        String compare = getName();
        String compareWith = specialties.getName();

        if (compare.charAt(0) < compareWith.charAt(0)) {
            return -1;
        } else if (compare.charAt(0) == compareWith.charAt(0)) {
            return 0;
        } else {
            return 1;
        }
    }
}
