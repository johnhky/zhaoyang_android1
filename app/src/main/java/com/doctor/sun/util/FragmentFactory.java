package com.doctor.sun.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.fragment.AllowToSearchFragment;
import com.doctor.sun.ui.fragment.AnswerQuestionFragment;
import com.doctor.sun.ui.fragment.ChangeMyPhoneNumFragment;
import com.doctor.sun.ui.fragment.EditDoctorInfoFragment;
import com.doctor.sun.ui.fragment.MyScalesInventoryFragment;
import com.doctor.sun.ui.fragment.QTemplatesFragment;
import com.doctor.sun.ui.fragment.QuestionStatsFragment;
import com.doctor.sun.ui.fragment.QuestionsInventoryFragment;
import com.doctor.sun.ui.fragment.ReadQTemplateFragment;
import com.doctor.sun.ui.fragment.ReadQuestionsFragment;
import com.doctor.sun.ui.fragment.RegisterFragment;
import com.doctor.sun.ui.fragment.ResetPswFragment;
import com.doctor.sun.ui.fragment.ScalesInventoryFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rick on 17/8/2016.
 */

public class FragmentFactory {
    private final Map<String, FactoryCommand> map = new HashMap<>();

    private static FragmentFactory instance;

    public static FragmentFactory getInstance() {
        if (instance == null) {
            instance = new FragmentFactory();
        }
        return instance;
    }

    public FactoryCommand get(String stringExtra) {
        return map.get(stringExtra);
    }

    public Fragment get(Bundle args) {
        String name = args.getString(Constants.FRAGMENT_NAME);
        FactoryCommand factoryCommand = FragmentFactory.getInstance().get(name);
        if (factoryCommand != null) {
            Fragment result = factoryCommand.execute();
            result.setArguments(args);
            return result;
        } else {
            throw new FragmentNotFoundException("Unable to find  fragment class {" + name + "}; have you register \n" +
                    "this fragment in FragmentFactory?**");
        }
    }

    public interface FactoryCommand {
        Fragment execute();
    }


    private FragmentFactory() {
        init();
    }

    private void init() {
        map.put(EditDoctorInfoFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new EditDoctorInfoFragment();
            }
        });
        map.put(ChangeMyPhoneNumFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new ChangeMyPhoneNumFragment();
            }
        });
        map.put(RegisterFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new RegisterFragment();
            }
        });
        map.put(AllowToSearchFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new AllowToSearchFragment();
            }
        });
        map.put(ResetPswFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new ResetPswFragment();
            }
        });

        map.put(AnswerQuestionFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new AnswerQuestionFragment();
            }
        });
        map.put(ReadQuestionsFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new ReadQuestionsFragment();
            }
        });
        map.put(ReadQTemplateFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new ReadQTemplateFragment();
            }
        });
        map.put(QTemplatesFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new QTemplatesFragment();
            }
        });
        map.put(QuestionStatsFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new QuestionStatsFragment();
            }
        });
        map.put(QuestionsInventoryFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new QuestionsInventoryFragment();
            }
        });
        map.put(MyScalesInventoryFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new MyScalesInventoryFragment();
            }
        });
        map.put(ScalesInventoryFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new ScalesInventoryFragment();
            }
        });
    }

    public class FragmentNotFoundException extends RuntimeException {
        public FragmentNotFoundException() {
        }

        public FragmentNotFoundException(String name) {
            super(name);
        }
    }
}
