package com.mifos.objects.templates.clients;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rajan on 13/3/16.
 */
public class ClientsTemplate {

	private int [] activationDate;
	private int officeId;
	private List<OfficeOptions> officeOptions;
	private List<StaffOptions> staffOptions;
	private List<SavingProductOptions> savingProductOptions;

	public int[] getActivationDate()
	{
		return activationDate;
	}

	public void setActivationDate(int[] activationDate)
	{
		this.activationDate = activationDate;
	}

	public int getOfficeId()
	{
		return officeId;
	}

	public void setOfficeId(int officeId)
	{
		this.officeId = officeId;
	}

	public List<OfficeOptions> getOfficeOptions()
	{
		return officeOptions;
	}

	public void setOfficeOptions(List<OfficeOptions> officeOptions)
	{
		this.officeOptions = officeOptions;
	}

	public List<StaffOptions> getStaffOptions()
	{
		return staffOptions;
	}

	public void setStaffOptions(List<StaffOptions> staffOptions)
	{
		this.staffOptions = staffOptions;
	}

	public List<SavingProductOptions> getSavingProductOptions()
	{
		return savingProductOptions;
	}

	public void setSavingProductOptions(List<SavingProductOptions> savingProductOptions)
	{
		this.savingProductOptions = savingProductOptions;
	}

	@Override
	public String toString()
	{
		return "ClientsTemplate{" +
				"activationDate=" + Arrays.toString(activationDate) +
				", officeId=" + officeId +
				", officeOptions=" + officeOptions +
				", staffOptions=" + staffOptions +
				", savingProductOptions=" + savingProductOptions +
				'}';
	}
}
