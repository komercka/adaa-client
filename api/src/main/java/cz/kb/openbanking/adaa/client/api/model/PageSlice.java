package cz.kb.openbanking.adaa.client.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Contains basic information about current page with elements.
 *
 * @param <T> type of elements on the page
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public class PageSlice<T> {

    /**
     * List of elements.
     */
    private List<T> content;

    /**
     * Total number of pages.
     */
    private int totalPages;

    /**
     * Current page number.
     */
    private int pageNumber;

    /**
     * PageSlice size.
     */
    private int pageSize;

    /**
     * Number of elements on the current page.
     */
    private int numberOfElements;

    /**
     * Is the first page.
     */
    private boolean first;

    /**
     * Is the last page.
     */
    private boolean last;

    /**
     * Is the current page empty.
     */
    private boolean empty;

    /**
     * New instance.
     *
     * @param content          list of elements
     * @param totalPages       total number of pages
     * @param pageNumber       number of page
     * @param pageSize         page size
     * @param numberOfElements number of elements on the current page
     * @param first            is the first page
     * @param last             is the last page
     * @param empty            is the current page empty
     */
    @JsonCreator
    public PageSlice(@JsonProperty("content") Collection<T> content, @JsonProperty("totalPages") int totalPages,
                     @JsonProperty("pageNumber") int pageNumber, @JsonProperty("pageSize") int pageSize,
                     @JsonProperty("numberOfElements") int numberOfElements, @JsonProperty("first") boolean first,
                     @JsonProperty("last") boolean last, @JsonProperty("empty") boolean empty)
    {
        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }
        if (totalPages < 0) {
            throw new IllegalArgumentException("totalPages must be greater or equal to zero");
        }
        if (pageNumber < 0) {
            throw new IllegalArgumentException("pageNumber must be greater or equal to zero");
        }
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize must be greater to zero");
        }
        if (numberOfElements < 0) {
            throw new IllegalArgumentException("numberOfElements must be greater or equal to zero");
        }

        this.content = new ArrayList<>(content);
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.numberOfElements = numberOfElements;
        this.first = first;
        this.last = last;
        this.empty = empty;
    }

    /**
     * Gets page content.
     *
     * @return page content
     */
    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    /**
     * Gets number of total pages.
     *
     * @return number of total pages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Gets number of the current page.
     *
     * @return number of the current page
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Gets page size.
     *
     * @return page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Gets number of all elements.
     *
     * @return number of all elements
     */
    public int getNumberOfElements() {
        return numberOfElements;
    }

    /**
     * Gets if the last page is the first one.
     *
     * @return if the last page is the first one
     */
    public boolean isFirst() {
        return first;
    }

    /**
     * Gets if the last page is the last one.
     *
     * @return if the last page is the last one
     */
    public boolean isLast() {
        return last;
    }

    /**
     * Gets if the last page is empty.
     *
     * @return if the last page is empty
     */
    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof PageSlice)) {
            return false;
        }

        final PageSlice<?> pageSlice = (PageSlice<?>) o;

        return new EqualsBuilder()
                .append(getTotalPages(), pageSlice.getTotalPages())
                .append(getPageNumber(), pageSlice.getPageNumber())
                .append(getPageSize(), pageSlice.getPageSize())
                .append(getNumberOfElements(), pageSlice.getNumberOfElements())
                .append(isFirst(), pageSlice.isFirst())
                .append(isLast(), pageSlice.isLast())
                .append(isEmpty(), pageSlice.isEmpty())
                .append(getContent(), pageSlice.getContent())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getContent())
                .append(getTotalPages())
                .append(getPageNumber())
                .append(getPageSize())
                .append(getNumberOfElements())
                .append(isFirst())
                .append(isLast())
                .append(isEmpty())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("content", content)
                .append("totalPages", totalPages)
                .append("pageNumber", pageNumber)
                .append("pageSize", pageSize)
                .append("numberOfElements", numberOfElements)
                .append("first", first)
                .append("last", last)
                .append("empty", empty)
                .toString();
    }
}
