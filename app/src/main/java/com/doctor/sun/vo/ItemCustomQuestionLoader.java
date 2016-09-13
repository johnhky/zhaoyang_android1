package com.doctor.sun.vo;

import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
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

import retrofit2.Call;

/**
 * Created by rick on 10/9/2016.
 */

public class ItemCustomQuestionLoader extends BaseItem {
    public static final String CUSTOM_QUESTION_LOADER = "CUSTOM_QUESTION_LOADER";
    public static final int FIRST_ITEM_POSITION_PADDING = 5000;
    private final String id;
    private boolean isInitialized = false;
    private boolean isExpended;
    private boolean isChildVisible = true;

    List<SortedItem> cache = new ArrayList<>();
    private QuestionModule questionModule = Api.of(QuestionModule.class);
    private QuestionsModel questionsModel = new QuestionsModel(FIRST_ITEM_POSITION_PADDING);
    private int systemQuestionsPage = 1;
    private int systemQuestionsCount = 0;
    private boolean finished;
    private Call<ApiDTO<PageDTO<Questions2>>> apiDTOCall;

    public ItemCustomQuestionLoader(int itemLayoutId, String id) {
        super(itemLayoutId);
        this.id = id;
    }

    public void onClick(SortedListAdapter adapter) {
        if (isExpended) {
            fold(adapter);
        } else {
            expend(adapter);
        }
    }

    private void restoredItems(SortedListAdapter adapter) {
        ItemSystemQuestionLoader item = (ItemSystemQuestionLoader) adapter.get(ItemSystemQuestionLoader.SYSTEM_QUESTION_LOADER);
        item.fold(adapter);
        adapter.insertAll(cache);
    }

    public void fold(SortedListAdapter adapter) {
        if (!isExpended) {
            return;
        }
        setExpended(false);
        ItemSystemQuestionLoader item = (ItemSystemQuestionLoader) adapter.get(ItemSystemQuestionLoader.SYSTEM_QUESTION_LOADER);
        adapter.clear();
        adapter.insert(this);
        if (item != null) {
            item.expend(adapter);
            adapter.insert(item);
        }
    }

    public void loadMore(final SortedListAdapter adapter) {
        apiDTOCall = questionModule.customQuestions(id, String.valueOf(systemQuestionsPage));
        apiDTOCall.enqueue(new SimpleCallback<PageDTO<Questions2>>() {
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
        item.setId("CUSTOM_LOAD_MORE");
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

    public boolean isExpended() {
        return isExpended;
    }

    public void setExpended(boolean expended) {
        if (isExpended != expended) {
            isExpended = expended;
            notifyChange();
        }
    }

    @Override
    public String getKey() {
        return CUSTOM_QUESTION_LOADER;
    }

    public int background() {
        if (isExpended) {
            return R.color.green;
        } else {
            return R.color.orange;
        }
    }

    @Override
    public int getLayoutId() {
        if (!isExpended()) {
            return R.layout.space_30dp;
        } else {
            return super.getLayoutId();
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
            if (item.getLayoutId() != R.layout.divider_1px_margint_13dp) {
                item.setVisible(false);
            }
        }
    }

    public void setVisible(BaseItem item) {
        if (item.getLayoutId() != R.layout.item_inventory_question) {
            if (item.getLayoutId() != R.layout.divider_1px_margint_13dp) {
                item.setVisible(true);
            }
        }
    }

    public void expend(SortedListAdapter adapter) {
        if (isExpended()) {
            return;
        }
        setExpended(true);
        restoredItems(adapter);
        if (!isInitialized) {
            loadMore(adapter);
        } else {
            initLoadMoreItem(adapter);
        }
        ItemSystemQuestionLoader item = (ItemSystemQuestionLoader) adapter.get(ItemSystemQuestionLoader.SYSTEM_QUESTION_LOADER);
        if (item != null) {
            item.fold(adapter);
        }
        notifyChange();
    }

}
