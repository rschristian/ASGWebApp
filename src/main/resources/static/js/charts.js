$(function () {
    Highcharts.setOptions({
        lang: {
            decimalPoint: '.',
            thousandsSep: ','
        }
    });

    drawSalesByTypeChart();
    drawSalesByRegionChart();
});


function drawSalesByRegionChart() {
    var salesByRegionChart = Highcharts.chart('salesByRegion', {
        chart: {
            type: 'pie',
            margin: 40,
            options3d: {
                enabled: true,
                alpha: 45,
                beta: 0
            }
        },
        title: {
            text: 'Candidates at stages'
        },
        tooltip: {
            pointFormat: "${point.y:,.0f}"
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                depth: 35
            }
        },
        series: [{
            name: 'Regions',
            colorByPoint:true,
            data: [{
                name: 'Drone type 1',
                y: /*[[${northeastSales}]]*/ 0
            },{
                name: 'Drone type 2',
                y: /*[[${southSales}]]*/ 0
            },{
                name: 'Drone type 3',
                y: /*[[${midwestSales}]]*/ 0
            },{
                name: 'Drone type 4',
                y: /*[[${westSales}]]*/ 0
            }]
        }]
    });
}

function drawSalesByTypeChart() {
    var salesByTypeChart = Highcharts.chart('salesByType', {
        chart: {
            type: 'column',
            margin: 75,
            options3d: {
                enabled: true,
                alpha: 15,
                beta: 15,
                depth: 110
            }
        },
        title: {
            text: 'Sales by Lure Type'
        },
        xAxis: {
            categories: ['May', 'June', 'July']
        },
        yAxis: {
            title: {
                text: 'Candidates'
            }
        },
        tooltip: {
            pointFormat: "${point.y:,.0f}"
        },
        plotOptions: {
            column: {
                depth: 60,
                stacking: true,
                grouping: false,
                groupZPadding: 10
            }
        },
        series: [{
            name: 'Ground school',
            data: /*[[${inshoreSales}]]*/ []
        }, {
            name: 'Flight training',
            data: /*[[${nearshoreSales}]]*/ []
        }, {
            name: 'Flight assesment',
            data: /*[[${offshoreSales}]]*/ []
        }]
    });
}