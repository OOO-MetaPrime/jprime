package mp.jprime.security.abac;

import mp.jprime.common.annotations.JPCond;
import mp.jprime.common.annotations.JPTime;
import mp.jprime.dataaccess.JPAction;
import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.JPSecuritySettings;
import mp.jprime.security.abac.annotations.*;
import mp.jprime.security.beans.JPAccessType;

import java.time.DayOfWeek;

@JPPolicySets(
    value = {
        @JPPolicySet(
            name = "Группа политик annotation-тест",
            qName = "jpPolicySet.annotationTest",
            jpClasses = {"test1", "test2"},
            policies = {
                @JPPolicy(
                    name = "Политика Создание",
                    qName = "jpPolicySet.annotationTest.create",
                    actions = {JPAction.CREATE},
                    subjectRules = {
                        @JPSubjectRule(
                            name = "Доступно только роли TEST_ROLE",
                            qName = "jpPolicySet.annotationTest.create.rule1",
                            role = @JPCond(in = {"TEST_ROLE"}),
                            effect = JPAccessType.PERMIT
                        )
                    },
                    environmentRules = {
                        @JPEnvironmentRule(
                            name = "Доступно только в течении для 2010-01-10",
                            qName = "jpPolicySet.annotationTest.create.envRule1",
                            time = @JPTime(
                                daysOfWeek = {DayOfWeek.MONDAY, DayOfWeek.THURSDAY},
                                fromTime = "10:00",
                                toTime = "17:59",
                                fromDateTime = "2010-01-10T00:00:00",
                                toDateTime = "2020-01-10T23:59:59"
                            )
                        )
                    }
                ),
                @JPPolicy(
                    name = "Политика Обновление",
                    qName = "jpPolicySet.annotationTest.update",
                    actions = {JPAction.READ, JPAction.UPDATE},
                    resourceRules = {
                        @JPResourceRule(
                            name = "Доступно только автору",
                            qName = "jpPolicySet.annotationTest.update.rule1",
                            attr = "userOwnerId",
                            cond = @JPCond(in = {FilterValue.AUTH_USERID}),
                            effect = JPAccessType.PERMIT
                        )
                    }
                ),

            }
        )
    }
)
public class JPAbacTestSettings implements JPSecuritySettings {
}
