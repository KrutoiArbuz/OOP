importConfig 'tasks-sem1.groovy'
importConfig 'students-24216.groovy'

course {
    assignments {
        check task: '1_1_2', group: '24216'

    }

    checkpoints {
        checkpoint name: 'КТ1', startDate: '2025-09-13', date: '2025-12-27'

    }

    settings {
        latePenalty        0.5
        buildTimeoutSeconds 300
        testTimeoutSeconds 120
        activityWeight     0.0

        semester(1) {
            startDate '2025-09-08'
            weeks     17
        }

        gradeThreshold min: 81, grade: 'отлично'
        gradeThreshold min: 63, grade: 'хорошо'
        gradeThreshold min: 44, grade: 'удовлетворительно'

        extraPoints task: '1_1_1', student: 'KrutoiArbuz', points: 1
    }
}
