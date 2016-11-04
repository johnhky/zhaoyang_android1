package com.doctor.sun.vo;

import com.doctor.sun.R;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.Try;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 10/9/2016.
 */

public class ItemSystemQuestionLoader extends BaseItem {
    public static final String SYSTEM_QUESTION_LOADER = "SYSTEM_QUESTION_LOADER";
    private String id;
    private String keyword;
    private boolean isInitialized = false;
    private boolean finished;
    private boolean isExpended;
    private int systemQuestionsPage = 1;
    private int systemQuestionsCount = 0;
    private boolean isChildVisible = true;

    List<SortedItem> cache = new ArrayList<>();
    private QuestionModule questionModule = Api.of(QuestionModule.class);
    private QuestionsModel questionsModel = new QuestionsModel();

    public ItemSystemQuestionLoader(int itemLayoutId, String id, String keyword) {
        super(itemLayoutId);
        this.id = id;
        this.keyword = keyword;
    }

    public void onClick(SortedListAdapter adapter) {
        if (isExpended()) {
            fold(adapter);
        } else {
            expend(adapter);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        isChildVisible = visible;
        for (SortedItem item : cache) {
            if (visible) {
                setVisible((BaseItem) item);
            } else {
                setInVisible((BaseItem) item);
            }

        }
    }

    public void setInVisible(BaseItem item) {
        if (item.getLayoutId() != R.layout.item_inventory_question) {
            if (item.getLayoutId() != R.layout.divider_1px_top13) {
                item.setVisible(false);
            }
        }
    }

    public void setVisible(BaseItem item) {
        if (item.getLayoutId() != R.layout.item_inventory_question) {
            if (item.getLayoutId() != R.layout.divider_1px_top13) {
                item.setVisible(true);
            }
        }
    }

    public void expend(SortedListAdapter adapter) {

        ItemCustomQuestionLoader item = (ItemCustomQuestionLoader) adapter.get(ItemCustomQuestionLoader.CUSTOM_QUESTION_LOADER);
        if (item != null) {
            item.fold(adapter);
        }
        restoredItems(adapter);
        if (!isInitialized) {
            loadMore(adapter);
        } else {
            initLoadMoreItem(adapter);
        }

        notifyChange();
    }

    private void restoredItems(SortedListAdapter adapter) {
        adapter.insertAll(cache);

        setExpended(true);
    }

    public boolean isExpended() {
        return isExpended;
    }

    public void setExpended(boolean expended) {
        if (isExpended != expended) {
            isExpended = expended;
            notifyChange();
        }
    }

    public void fold(SortedListAdapter adapter) {
        if (!isExpended) {
            return;
        }
        setExpended(false);
        ItemCustomQuestionLoader item = (ItemCustomQuestionLoader) adapter.get(ItemCustomQuestionLoader.CUSTOM_QUESTION_LOADER);
        adapter.clear();
        adapter.insert(this);
        if (item != null) {
            adapter.insert(item);
            item.expend(adapter);
        }
    }

    public void loadMore(final SortedListAdapter adapter) {
        questionModule.systemQuestions(id, String.valueOf(systemQuestionsPage), keyword).enqueue(new SimpleCallback<PageDTO<Questions2>>() {
            @Override
            protected void handleResponse(PageDTO<Questions2> response) {
                isInitialized = true;
                List<Questions2> data = response.getData();
                if (data != null && !data.isEmpty()) {
                    List<SortedItem> items = questionsModel.parseQuestions(data, 0, systemQuestionsCount);

                    if (!isChildVisible) {
                        for (SortedItem item : items) {
                            BaseItem baseItem = (BaseItem) item;
                            setInVisible(baseItem);
                        }
                    }

                    cache.addAll(items);
                    adapter.insertAll(items);
                    systemQuestionsCount += questionsModel.questionsSize(data, 0, 0);
                    systemQuestionsPage += 1;
                }
                int to = response.getTo();
                int total = response.getTotal();
                int perPage = response.getPerPage();
                finished = to >= total || (systemQuestionsPage - 1) * perPage >= total;
                initLoadMoreItem(adapter);
            }
        });
    }

    private void initLoadMoreItem(SortedListAdapter adapter) {
        if (!finished) {
            insertLoadMore(adapter);
        } else {
            adapter.removeItem(new ItemLoadMore());
        }
    }

    private void insertLoadMore(final SortedListAdapter adapter) {
        final ItemLoadMore item = new ItemLoadMore();
        item.setLoadMoreListener(new Try() {
            @Override
            public void success() {
                loadMore(adapter);
            }

            @Override
            public void fail() {
            }
        });
        adapter.insert(item);
    }

    public String getAction() {
        if (isExpended) {
            return "收起";
        } else {
            return "展开";
        }
    }

    @Override
    public String getKey() {
        return SYSTEM_QUESTION_LOADER;
    }

    public int background() {
        if (isExpended) {
            return R.color.green;
        } else {
            return R.color.orange;
        }
    }
}
