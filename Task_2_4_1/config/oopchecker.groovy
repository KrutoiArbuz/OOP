importConfig 'tasks-sem1.groovy'
importConfig 'tasks-sem2.groovy'
importConfig 'students-24216.groovy'
importConfig 'students-24214.groovy'

course {
    assignments {
//        check task: '1_1_1', group: '24216'
//        check task: '1_1_2', group: '24216'
//        check task: '1_1_3', group: '24216'
//        check task: '1_2_1', group: '24216'
//        check task: '1_2_2', group: '24216'
//        check task: '1_3_1', group: '24216'
//        check task: '1_4_1', group: '24216'
//        check task: '1_5_1', group: '24216'
//        check task: '2_1_1', group: '24216'
//        check task: '2_1_2', group: '24216'
//        check task: '2_2_1', group: '24216'
//        check task: '2_3_1', group: '24216'
//        check task: '2_4_1', group: '24216'
        check task: '1_1_1', group: '24214'
        check task: '1_1_2', group: '24214'
        check task: '1_1_3', group: '24214'
        check task: '1_2_1', group: '24214'
        check task: '1_2_2', group: '24214'
        check task: '1_3_1', group: '24214'
        check task: '1_4_1', group: '24214'
        check task: '1_5_1', group: '24214'
        check task: '2_1_1', group: '24214'
        check task: '2_1_2', group: '24214'
        check task: '2_2_1', group: '24214'
        check task: '2_3_1', group: '24214'
        check task: '2_4_1', group: '24214'
    }

    checkpoints {
        checkpoint name: 'КТ1', startDate: '2025-09-13', date: '2025-12-27'
        checkpoint name: 'КТ2', startDate: '2026-02-14', date: '2026-05-30'
    }

    settings {
        latePenalty     0.5
        testTimeoutSeconds 120
        semesterStart   '2026-02-02'
        semesterWeeks   17
        activityWeight  0.0


        gradeThreshold min: 81, grade: 'отлично'
        gradeThreshold min: 63, grade: 'хорошо'
        gradeThreshold min: 44, grade: 'удовлетворительно'



        extraPoints task: '1_1_3', student: 'KrutoiArbuz',  points: 1
        extraPoints task: '2_3_1', student: 'KrutoiArbuz',  points: 1

    }
}
