course {
    tasks {
        task {
            id '2_1_1'
            name 'Простые числа'
            maxPoints 10
            softDeadline '2023-10-01'
            hardDeadline '2023-10-15'
        }
        task {
            id '2_3_1'
            name 'Змейка'
            maxPoints 20
            softDeadline '2023-11-01'
            hardDeadline '2023-11-15'
        }
    }

    groups {
        group('12345') {
            student github: 'ivanov', name: 'Иванов И.И.',
                    repo: 'https://github.com/ivanov/oop.git'
            student github: 'petrov', name: 'Петров П.П.',
                    repo: 'https://github.com/petrov/oop.git'
        }
    }

    assignments {
        check task: '2_1_1', student: 'ivanov'
        check task: '2_1_1', student: 'petrov'
        check task: '2_3_1', student: 'ivanov'
    }

    checkpoints {
        checkpoint name: 'КТ1', date: '2023-10-31'
        checkpoint name: 'КТ2', date: '2023-12-15'
    }

    settings {
        latePenalty 0.5
        testTimeoutSeconds 60
        activityWeight 0.2
        gradeThreshold min: 85, grade: 'отлично'
        gradeThreshold min: 70, grade: 'хорошо'
        gradeThreshold min: 50, grade: 'удовлетворительно'
        extraPoints task: '2_3_1', student: 'ivanov', points: 2
    }
}
