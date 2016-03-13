package com.mifos.objects.templates.clients;

/**
 * Created by rajan on 13/3/16.
 */
public class SavingProductOptions {

	private int id;
	private String name;
	private boolean withdrawalFeeForTransfers;
	private boolean allowOverdraft;
	private boolean enforceMinRequiredBalance;

	public boolean isEnforceMinRequiredBalance() {
		return enforceMinRequiredBalance;
	}

	public void setEnforceMinRequiredBalance(boolean enforceMinRequiredBalance) {
		this.enforceMinRequiredBalance = enforceMinRequiredBalance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isWithdrawalFeeForTransfers() {
		return withdrawalFeeForTransfers;
	}

	public void setWithdrawalFeeForTransfers(boolean withdrawalFeeForTransfers) {
		this.withdrawalFeeForTransfers = withdrawalFeeForTransfers;
	}

	public boolean isAllowOverdraft() {
		return allowOverdraft;
	}

	public void setAllowOverdraft(boolean allowOverdraft) {
		this.allowOverdraft = allowOverdraft;
	}

	@Override
	public String toString() {
		return "SavingProductOptions{" +
				"id=" + id +
				", name='" + name + '\'' +
				", withdrawalFeeForTransfers=" + withdrawalFeeForTransfers +
				", allowOverdraft=" + allowOverdraft +
				", enforceMinRequiredBalance=" + enforceMinRequiredBalance +
				'}';
	}
}
