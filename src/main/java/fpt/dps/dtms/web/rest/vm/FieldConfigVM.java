package fpt.dps.dtms.web.rest.vm;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model object for storing column header
 */
public class FieldConfigVM {

    private String position;
    
    private String inputType;
    
    private Object options;
    
    private String urlOption;
    
    private String paramOptions;
    
	private String reuseData;

	private Object collections;
    
    private String type;
    
    private String value;
    
    private String valueGetter;
    
	private Properties properties;
    
    private List<Validator> validations;
    
    private String headerName;
    
    private String field;
    
    private Integer width;
    
    private String pinned;
    
    private String cellRenderer;
    
    private String cellEditor;
    
	private String format;
    
    private String colId;
    
    private boolean editable;
    
    private String filter;
    
    private boolean hide;
    
    private boolean sortable;
    
    private String sort;
    
    private int displayPosition;
    
    private int sortByPosition;
    
	private Object cellRendererParams;
    
    private boolean enableFilter;
    
    private boolean rowDrag;
    
    private boolean hideDynamicField;
    
    private boolean checkboxSelection;
    
    private boolean isSelected;
    
    private boolean headerCheckboxSelection;
    
    private boolean suppressRowClickSelection;
    
    private String rowSelection;
    
    private String cellStyle;
    
    private String backgroundColor;
    
    private String color;

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public Object getOptions() {
		return options;
	}

	public void setOptions(Object options) {
		this.options = options;
	}

	public String getUrlOption() {
		return urlOption;
	}

	public void setUrlOption(String urlOption) {
		this.urlOption = urlOption;
	}
	
	public String getParamOptions() {
		return paramOptions;
	}

	public void setParamOptions(String paramOptions) {
		this.paramOptions = paramOptions;
	}
	
	public String getReuseData() {
		return reuseData;
	}

	public void setReuseData(String reuseData) {
		this.reuseData = reuseData;
	}
	
	public Object getCollections() {
		return collections;
	}

	public void setCollections(Object collections) {
		this.collections = collections;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueGetter() {
		return valueGetter;
	}

	public void setValueGetter(String valueGetter) {
		this.valueGetter = valueGetter;
	}
	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public List<Validator> getValidations() {
		return validations;
	}

	public void setValidations(List<Validator> validations) {
		this.validations = validations;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getPinned() {
		return pinned;
	}

	public void setPinned(String pinned) {
		this.pinned = pinned;
	}

	public String getCellRenderer() {
		return cellRenderer;
	}

	public void setCellRenderer(String cellRenderer) {
		this.cellRenderer = cellRenderer;
	}
	
	public String getCellEditor() {
		return cellEditor;
	}

	public void setCellEditor(String cellEditor) {
		this.cellEditor = cellEditor;
	}
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getColId() {
		return colId;
	}

	public void setColId(String colId) {
		this.colId = colId;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getDisplayPosition() {
		return displayPosition;
	}

	public void setDisplayPosition(int displayPosition) {
		this.displayPosition = displayPosition;
	}
	
	public int getSortByPosition() {
		return sortByPosition;
	}

	public void setSortByPosition(int sortByPosition) {
		this.sortByPosition = sortByPosition;
	}
	
	public Object getCellRendererParams() {
		return cellRendererParams;
	}

	public void setCellRendererParams(Object cellRendererParams) {
		this.cellRendererParams = cellRendererParams;
	}

	public boolean isEnableFilter() {
		return enableFilter;
	}

	public void setEnableFilter(boolean enableFilter) {
		this.enableFilter = enableFilter;
	}

	public boolean isRowDrag() {
		return rowDrag;
	}

	public void setRowDrag(boolean rowDrag) {
		this.rowDrag = rowDrag;
	}

	public boolean isHideDynamicField() {
		return hideDynamicField;
	}

	public void setHideDynamicField(boolean hideDynamicField) {
		this.hideDynamicField = hideDynamicField;
	}

	public boolean isCheckboxSelection() {
		return checkboxSelection;
	}

	public void setCheckboxSelection(boolean checkboxSelection) {
		this.checkboxSelection = checkboxSelection;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isHeaderCheckboxSelection() {
		return headerCheckboxSelection;
	}

	public void setHeaderCheckboxSelection(boolean headerCheckboxSelection) {
		this.headerCheckboxSelection = headerCheckboxSelection;
	}

	public boolean isSuppressRowClickSelection() {
		return suppressRowClickSelection;
	}

	public void setSuppressRowClickSelection(boolean suppressRowClickSelection) {
		this.suppressRowClickSelection = suppressRowClickSelection;
	}

	public String getRowSelection() {
		return rowSelection;
	}

	public void setRowSelection(String rowSelection) {
		this.rowSelection = rowSelection;
	}

	public String getCellStyle() {
		return cellStyle;
	}

	public void setCellStyle(String cellStyle) {
		this.cellStyle = cellStyle;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
}
