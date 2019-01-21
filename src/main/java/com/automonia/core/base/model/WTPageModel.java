package com.automonia.core.base.model;


import java.util.ArrayList;
import java.util.List;

/**
 * 数据分页模型对象
 *
 * @author 作者 温腾
 * @创建时间 2017年5月9日 上午12:13:28
 */
public class WTPageModel<T extends WTModel> {

    private WTPage page;

    /**
     * 数据集合
     */
    private List<T> datas;

    public WTPage getPage() {
        return page;
    }

    public void setPage(WTPage page) {
        this.page = page;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    /**
     * 根据列表数据和数据总数初始化WTPageModel对象
     *
     * @param datas      查询的数据列表(分页)
     * @param totalCount 查询数据总数
     * @return 对象本身
     * @Param pageNo 当前页码
     */
    public WTPageModel<T> initPageModel(List<T> datas, Integer totalCount, Integer pageNo, Integer pageSize) {
        setDatas((datas == null ? new ArrayList<>() : datas));

        page = new WTPage();

        page.setPageSize(pageSize);
        page.setTotalCount((totalCount == null || totalCount <= 0) ? 0 : totalCount);
        page.setPageNo((pageNo == null || pageNo <= 0) ? 1 : pageNo);

        //计算页数
        Integer pageCountRoundup = roundup(totalCount, page.getPageSize());
        page.setPageCount(pageCountRoundup <= 0 ? 1 : pageCountRoundup);

        // 设置当前数据集的开始下标和结束下标
        page.setStartIndex((page.getPageNo() - 1) * page.getPageSize());
        page.setEndIndex(page.getPageNo() * page.getPageSize() - 1);
        if (page.getEndIndex() > page.getTotalCount()) {
            page.setEndIndex(page.getTotalCount() - 1);
        }

        //页码小于总页码数，说明有下一页，否则没有
        page.setHasNextPage(page.getPageNo() < page.getPageCount());

        //页码比1大，说明有上一页，否则没有
        page.setHasPreviousPage(page.getPageNo() > 1);

        return this;
    }

    private Integer roundup(Integer value1, Integer value2) {
        if (value2 == null || value2 == 0) {
            return 0;
        }
        Integer value = value1 / value2;
        if (value1 % value2 > 0) {
            value += 1;
        }
        return value;
    }

}
















