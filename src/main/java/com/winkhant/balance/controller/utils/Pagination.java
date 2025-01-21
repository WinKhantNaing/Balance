package com.winkhant.balance.controller.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagination {

	private int currentPage;
	private int totalPages;
	private boolean first;
	private boolean last;
	private String url;
	private String sizeChangeFormId;
	private Map<String, String> params;

	private List<Integer> pages;
	private List<Integer> sizes;
	
	public boolean isShow() {
		return pages.size() > 1;
	}

	public static Builder builder(String url) {
		return new Builder(url);
	}

	public static class Builder {
		private int currentPage;
		private int totalPages;
		private boolean first;
		private boolean last;
		private String url;
		private String sizeChangeFormId;
		private Map<String, String> params;
		private List<Integer> sizes;

		public Builder(String url) {
			this.url = url;
		}

		public <T> Builder page(Page<T> page) {
			this.currentPage = page.getNumber();
			this.totalPages = page.getTotalPages();
			this.first = page.isFirst();
			this.last = page.isLast();
			 	
			return this;
		}

		public Builder params(Map<String, String> params) {
			this.params = params;
			return this;
		}

		public Builder cureentPage(int currentPage) {
			this.currentPage = currentPage;
			return this;
		}

		public Builder totalPages(int totalPages) {
			this.totalPages = totalPages;
			return this;
		}

		public Builder first(boolean first) {
			this.first = first;
			return this;
		}
		
		public Builder sizes(List<Integer> sizes) {
			this.sizes = sizes;
			return this;
		}

		public Builder last(boolean last) {
			this.last = last;
			return this;
		}
		
		public Builder sizeChangeFormId(String sizeChangeFormId) {
			this.sizeChangeFormId = sizeChangeFormId;
			return this;
		}

		public Pagination build() {
			return new Pagination(currentPage, totalPages, first, last, url, params,sizes,sizeChangeFormId);
		}

	}

	private Pagination(int currentPage, int totalPages, boolean first, boolean last, String url,
			Map<String, String> params, List<Integer> sizes, String sizeChangeFormId) {

		this.currentPage = currentPage;
		this.totalPages = totalPages;
		this.first = first;
		this.last = last;
		this.url = url;
		this.params = null == params ? new HashMap<String, String>() : params;
		this.sizes = null == sizes ? new ArrayList<Integer>() : sizes;
		this.sizeChangeFormId = sizeChangeFormId;


		 pages = new ArrayList<>();

		    if (totalPages > 0) {
		        pages.add(this.currentPage);

		        // Populate pages before the current page
		        while (pages.size() < 5 && pages.get(0) > 0) {
		            pages.add(0, pages.get(0) - 1);
		        }

		        // Populate pages after the current page
		        while (pages.size() < 5 && pages.get(pages.size() - 1) < totalPages - 1) {
		            pages.add(pages.get(pages.size() - 1) + 1);
		        }
		    }
	}

	public String getParams() {
		return params.entrySet().stream().map(a -> "%s=%s".formatted(a.getKey(), a.getValue())).reduce("",
				(a, b) -> "%s&%s".formatted(a, b));
	}

}
