package com.c4me.server.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "College", schema = "siyoliu")
public class CollegeEntity {
    private int id;
    private String name;
    private String type;
    private Double averageGpa;
    private Double admissionRate;
    private String city;
    private String state;
    private String webpage;
    private Double latitude;
    private Double longitude;
    private Integer instateTuition;
    private Integer outstateTuition;
    private Double netPrice;
    private Double medianDebt;
    private Integer numStudentsEnrolled;
    private Double retentionRate;
    private Double completionRate;
    private Double meanEarnings;
    private Timestamp updatedTime;
    private Integer ranking;
    private Integer satMath25;
    private Integer satMath50;
    private Integer satMath75;
    private Integer satEbrw25;
    private Integer satEbrw50;
    private Integer satEbrw75;
    private Integer satOverall;
    private Integer actMath25;
    private Integer actMath50;
    private Integer actMath75;
    private Integer actEnglish25;
    private Integer actEnglish50;
    private Integer actEnglish75;
    private Integer actReading25;
    private Integer actReading50;
    private Integer actReading75;
    private Integer actScience25;
    private Integer actScience50;
    private Integer actScience75;
    private Integer actComposite;
    private Collection<CollegeHighschoolAssociationEntity> collegeHighschoolAssociationsById;
    private Collection<CollegeMajorAssociationEntity> collegeMajorAssociationsById;
    private Collection<StudentApplicationEntity> studentApplicationsById;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 128)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "type", nullable = true, length = 45)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "average_gpa", nullable = true, precision = 0)
    public Double getAverageGpa() {
        return averageGpa;
    }

    public void setAverageGpa(Double averageGpa) {
        this.averageGpa = averageGpa;
    }

    @Basic
    @Column(name = "admission_rate", nullable = true, precision = 0)
    public Double getAdmissionRate() {
        return admissionRate;
    }

    public void setAdmissionRate(Double admissionRate) {
        this.admissionRate = admissionRate;
    }

    @Basic
    @Column(name = "city", nullable = true, length = 45)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "state", nullable = true, length = 45)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Basic
    @Column(name = "webpage", nullable = true, length = 512)
    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    @Basic
    @Column(name = "latitude", nullable = true, precision = 0)
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "longitude", nullable = true, precision = 0)
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "instate_tuition", nullable = true)
    public Integer getInstateTuition() {
        return instateTuition;
    }

    public void setInstateTuition(Integer instateTuition) {
        this.instateTuition = instateTuition;
    }

    @Basic
    @Column(name = "outstate_tuition", nullable = true)
    public Integer getOutstateTuition() {
        return outstateTuition;
    }

    public void setOutstateTuition(Integer outstateTuition) {
        this.outstateTuition = outstateTuition;
    }

    @Basic
    @Column(name = "net_price", nullable = true, precision = 0)
    public Double getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(Double netPrice) {
        this.netPrice = netPrice;
    }

    @Basic
    @Column(name = "median_debt", nullable = true, precision = 0)
    public Double getMedianDebt() {
        return medianDebt;
    }

    public void setMedianDebt(Double medianDebt) {
        this.medianDebt = medianDebt;
    }

    @Basic
    @Column(name = "num_students_enrolled", nullable = true)
    public Integer getNumStudentsEnrolled() {
        return numStudentsEnrolled;
    }

    public void setNumStudentsEnrolled(Integer numStudentsEnrolled) {
        this.numStudentsEnrolled = numStudentsEnrolled;
    }

    @Basic
    @Column(name = "retention_rate", nullable = true, precision = 0)
    public Double getRetentionRate() {
        return retentionRate;
    }

    public void setRetentionRate(Double retentionRate) {
        this.retentionRate = retentionRate;
    }

    @Basic
    @Column(name = "completion_rate", nullable = true, precision = 0)
    public Double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(Double completionRate) {
        this.completionRate = completionRate;
    }

    @Basic
    @Column(name = "mean_earnings", nullable = true, precision = 0)
    public Double getMeanEarnings() {
        return meanEarnings;
    }

    public void setMeanEarnings(Double meanEarnings) {
        this.meanEarnings = meanEarnings;
    }

    @Basic
    @Column(name = "updated_time", nullable = true)
    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Basic
    @Column(name = "ranking", nullable = true)
    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    @Basic
    @Column(name = "SAT_math25", nullable = true)
    public Integer getSatMath25() {
        return satMath25;
    }

    public void setSatMath25(Integer satMath25) {
        this.satMath25 = satMath25;
    }

    @Basic
    @Column(name = "SAT_math50", nullable = true)
    public Integer getSatMath50() {
        return satMath50;
    }

    public void setSatMath50(Integer satMath50) {
        this.satMath50 = satMath50;
    }

    @Basic
    @Column(name = "SAT_math75", nullable = true)
    public Integer getSatMath75() {
        return satMath75;
    }

    public void setSatMath75(Integer satMath75) {
        this.satMath75 = satMath75;
    }

    @Basic
    @Column(name = "SAT_EBRW25", nullable = true)
    public Integer getSatEbrw25() {
        return satEbrw25;
    }

    public void setSatEbrw25(Integer satEbrw25) {
        this.satEbrw25 = satEbrw25;
    }

    @Basic
    @Column(name = "SAT_EBRW50", nullable = true)
    public Integer getSatEbrw50() {
        return satEbrw50;
    }

    public void setSatEbrw50(Integer satEbrw50) {
        this.satEbrw50 = satEbrw50;
    }

    @Basic
    @Column(name = "SAT_EBRW75", nullable = true)
    public Integer getSatEbrw75() {
        return satEbrw75;
    }

    public void setSatEbrw75(Integer satEbrw75) {
        this.satEbrw75 = satEbrw75;
    }

    @Basic
    @Column(name = "SAT_overall", nullable = true)
    public Integer getSatOverall() {
        return satOverall;
    }

    public void setSatOverall(Integer satOverall) {
        this.satOverall = satOverall;
    }

    @Basic
    @Column(name = "ACT_math25", nullable = true)
    public Integer getActMath25() {
        return actMath25;
    }

    public void setActMath25(Integer actMath25) {
        this.actMath25 = actMath25;
    }

    @Basic
    @Column(name = "ACT_math50", nullable = true)
    public Integer getActMath50() {
        return actMath50;
    }

    public void setActMath50(Integer actMath50) {
        this.actMath50 = actMath50;
    }

    @Basic
    @Column(name = "ACT_math75", nullable = true)
    public Integer getActMath75() {
        return actMath75;
    }

    public void setActMath75(Integer actMath75) {
        this.actMath75 = actMath75;
    }

    @Basic
    @Column(name = "ACT_english25", nullable = true)
    public Integer getActEnglish25() {
        return actEnglish25;
    }

    public void setActEnglish25(Integer actEnglish25) {
        this.actEnglish25 = actEnglish25;
    }

    @Basic
    @Column(name = "ACT_english50", nullable = true)
    public Integer getActEnglish50() {
        return actEnglish50;
    }

    public void setActEnglish50(Integer actEnglish50) {
        this.actEnglish50 = actEnglish50;
    }

    @Basic
    @Column(name = "ACT_english75", nullable = true)
    public Integer getActEnglish75() {
        return actEnglish75;
    }

    public void setActEnglish75(Integer actEnglish75) {
        this.actEnglish75 = actEnglish75;
    }

    @Basic
    @Column(name = "ACT_reading25", nullable = true)
    public Integer getActReading25() {
        return actReading25;
    }

    public void setActReading25(Integer actReading25) {
        this.actReading25 = actReading25;
    }

    @Basic
    @Column(name = "ACT_reading50", nullable = true)
    public Integer getActReading50() {
        return actReading50;
    }

    public void setActReading50(Integer actReading50) {
        this.actReading50 = actReading50;
    }

    @Basic
    @Column(name = "ACT_reading75", nullable = true)
    public Integer getActReading75() {
        return actReading75;
    }

    public void setActReading75(Integer actReading75) {
        this.actReading75 = actReading75;
    }

    @Basic
    @Column(name = "ACT_science25", nullable = true)
    public Integer getActScience25() {
        return actScience25;
    }

    public void setActScience25(Integer actScience25) {
        this.actScience25 = actScience25;
    }

    @Basic
    @Column(name = "ACT_science50", nullable = true)
    public Integer getActScience50() {
        return actScience50;
    }

    public void setActScience50(Integer actScience50) {
        this.actScience50 = actScience50;
    }

    @Basic
    @Column(name = "ACT_science75", nullable = true)
    public Integer getActScience75() {
        return actScience75;
    }

    public void setActScience75(Integer actScience75) {
        this.actScience75 = actScience75;
    }

    @Basic
    @Column(name = "ACT_composite", nullable = true)
    public Integer getActComposite() {
        return actComposite;
    }

    public void setActComposite(Integer actComposite) {
        this.actComposite = actComposite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollegeEntity that = (CollegeEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(averageGpa, that.averageGpa) &&
                Objects.equals(admissionRate, that.admissionRate) &&
                Objects.equals(city, that.city) &&
                Objects.equals(state, that.state) &&
                Objects.equals(webpage, that.webpage) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude) &&
                Objects.equals(instateTuition, that.instateTuition) &&
                Objects.equals(outstateTuition, that.outstateTuition) &&
                Objects.equals(netPrice, that.netPrice) &&
                Objects.equals(medianDebt, that.medianDebt) &&
                Objects.equals(numStudentsEnrolled, that.numStudentsEnrolled) &&
                Objects.equals(retentionRate, that.retentionRate) &&
                Objects.equals(completionRate, that.completionRate) &&
                Objects.equals(meanEarnings, that.meanEarnings) &&
                Objects.equals(updatedTime, that.updatedTime) &&
                Objects.equals(ranking, that.ranking) &&
                Objects.equals(satMath25, that.satMath25) &&
                Objects.equals(satMath50, that.satMath50) &&
                Objects.equals(satMath75, that.satMath75) &&
                Objects.equals(satEbrw25, that.satEbrw25) &&
                Objects.equals(satEbrw50, that.satEbrw50) &&
                Objects.equals(satEbrw75, that.satEbrw75) &&
                Objects.equals(satOverall, that.satOverall) &&
                Objects.equals(actMath25, that.actMath25) &&
                Objects.equals(actMath50, that.actMath50) &&
                Objects.equals(actMath75, that.actMath75) &&
                Objects.equals(actEnglish25, that.actEnglish25) &&
                Objects.equals(actEnglish50, that.actEnglish50) &&
                Objects.equals(actEnglish75, that.actEnglish75) &&
                Objects.equals(actReading25, that.actReading25) &&
                Objects.equals(actReading50, that.actReading50) &&
                Objects.equals(actReading75, that.actReading75) &&
                Objects.equals(actScience25, that.actScience25) &&
                Objects.equals(actScience50, that.actScience50) &&
                Objects.equals(actScience75, that.actScience75) &&
                Objects.equals(actComposite, that.actComposite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, averageGpa, admissionRate, city, state, webpage, latitude, longitude, instateTuition, outstateTuition, netPrice, medianDebt, numStudentsEnrolled, retentionRate, completionRate, meanEarnings, updatedTime, ranking, satMath25, satMath50, satMath75, satEbrw25, satEbrw50, satEbrw75, satOverall, actMath25, actMath50, actMath75, actEnglish25, actEnglish50, actEnglish75, actReading25, actReading50, actReading75, actScience25, actScience50, actScience75, actComposite);
    }

    @OneToMany(mappedBy = "collegeByCollegeId")
    public Collection<CollegeHighschoolAssociationEntity> getCollegeHighschoolAssociationsById() {
        return collegeHighschoolAssociationsById;
    }

    public void setCollegeHighschoolAssociationsById(Collection<CollegeHighschoolAssociationEntity> collegeHighschoolAssociationsById) {
        this.collegeHighschoolAssociationsById = collegeHighschoolAssociationsById;
    }

    @OneToMany(mappedBy = "collegeByCollegeId")
    public Collection<CollegeMajorAssociationEntity> getCollegeMajorAssociationsById() {
        return collegeMajorAssociationsById;
    }

    public void setCollegeMajorAssociationsById(Collection<CollegeMajorAssociationEntity> collegeMajorAssociationsById) {
        this.collegeMajorAssociationsById = collegeMajorAssociationsById;
    }

    @OneToMany(mappedBy = "collegeByCollegeId")
    public Collection<StudentApplicationEntity> getStudentApplicationsById() {
        return studentApplicationsById;
    }

    public void setStudentApplicationsById(Collection<StudentApplicationEntity> studentApplicationsById) {
        this.studentApplicationsById = studentApplicationsById;
    }
}
