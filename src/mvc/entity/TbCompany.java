package mvc.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TbCompany entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tb_company", catalog = "xnjgpt")
public class TbCompany implements java.io.Serializable {

	// Fields

	private String companyId;
	private String companyEname;
	private String companyName;
	private String identifier;
	private String contactPhone;
	private Integer address;
	private String businessScope;
	private String contactAddress;
	private String economicType;
	private String regCapital;
	private String legalName;
	private String legalId;
	private String legalPhone;
	private String legalPhoto;
	private String identifierPhoto;
	private Integer state;
	private Integer flag;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public TbCompany() {
	}

	/** full constructor */
	public TbCompany(String companyEname, String companyName,
			String identifier, String contactPhone, Integer address,
			String businessScope, String contactAddress, String economicType,
			String regCapital, String legalName, String legalId,
			String legalPhone, String legalPhoto, String identifierPhoto,
			Integer state, Integer flag, Date updateTime) {
		this.companyEname = companyEname;
		this.companyName = companyName;
		this.identifier = identifier;
		this.contactPhone = contactPhone;
		this.address = address;
		this.businessScope = businessScope;
		this.contactAddress = contactAddress;
		this.economicType = economicType;
		this.regCapital = regCapital;
		this.legalName = legalName;
		this.legalId = legalId;
		this.legalPhone = legalPhone;
		this.legalPhoto = legalPhoto;
		this.identifierPhoto = identifierPhoto;
		this.state = state;
		this.flag = flag;
		this.updateTime = updateTime;
	}

	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "CompanyId", unique = true, nullable = false, length = 32)
	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Column(name = "CompanyEName")
	public String getCompanyEname() {
		return this.companyEname;
	}

	public void setCompanyEname(String companyEname) {
		this.companyEname = companyEname;
	}

	@Column(name = "CompanyName", length = 256)
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Column(name = "Identifier", length = 32)
	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Column(name = "ContactPhone", length = 32)
	public String getContactPhone() {
		return this.contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	@Column(name = "Address")
	public Integer getAddress() {
		return this.address;
	}

	public void setAddress(Integer address) {
		this.address = address;
	}

	@Column(name = "BusinessScope", length = 256)
	public String getBusinessScope() {
		return this.businessScope;
	}

	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}

	@Column(name = "ContactAddress", length = 256)
	public String getContactAddress() {
		return this.contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	@Column(name = "EconomicType", length = 128)
	public String getEconomicType() {
		return this.economicType;
	}

	public void setEconomicType(String economicType) {
		this.economicType = economicType;
	}

	@Column(name = "RegCapital", length = 128)
	public String getRegCapital() {
		return this.regCapital;
	}

	public void setRegCapital(String regCapital) {
		this.regCapital = regCapital;
	}

	@Column(name = "LegalName", length = 256)
	public String getLegalName() {
		return this.legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	@Column(name = "LegalID", length = 32)
	public String getLegalId() {
		return this.legalId;
	}

	public void setLegalId(String legalId) {
		this.legalId = legalId;
	}

	@Column(name = "LegalPhone", length = 32)
	public String getLegalPhone() {
		return this.legalPhone;
	}

	public void setLegalPhone(String legalPhone) {
		this.legalPhone = legalPhone;
	}

	@Column(name = "LegalPhoto", length = 128)
	public String getLegalPhoto() {
		return this.legalPhoto;
	}

	public void setLegalPhoto(String legalPhoto) {
		this.legalPhoto = legalPhoto;
	}

	@Column(name = "IdentifierPhoto", length = 128)
	public String getIdentifierPhoto() {
		return this.identifierPhoto;
	}

	public void setIdentifierPhoto(String identifierPhoto) {
		this.identifierPhoto = identifierPhoto;
	}

	@Column(name = "State")
	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Column(name = "Flag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Column(name = "UpdateTime", length = 19)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}